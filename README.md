# AndroidStartUp

玩具工程

想优化下项目内的启动框架，还是基于有向无环图将任务排序。之后拆分多线程等待执行任务，保证所有初始化任务能够有序的执行，不产生由于别的sdk没有初始化导致的错乱问题。

因为原来的项目是多进程，所以考虑给多进程解耦，开发了一套ksp的注解生成分组逻辑。不同进程可以生成不同的任务分组逻辑。

增加锚点逻辑，让任务链可以更便捷的添加。

支持多线程真等待执行，解决了任务只是按照优先顺序放入，并不会等待前置任务完成，多线程情况下可能出现异常的情况

# 依赖tag化

剔除了传统的类形式的依赖方式，转化成tag，标识当前任务的依赖。

好处就是多模块解耦，劣势就是更加抽象，并不能形成立体的依赖关系。

# task分组

只是增加了个简单的组的概念而已，内部的依赖顺序还是要重新调整。

该功能主要就是服务大型工程，多进程初始化，并且初始化任务复杂，但是同时不同进程间需要初始化的任务也不同。

## 使用

通过ksp，以及添加注解的方式完成组任务生成。

1. build.gradle 添加

```gradle
plugins {
    id("com.google.devtools.ksp") version "1.5.30-1.0.0"
}
kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}
dependencies {
    implementation project(':startup')
    implementation project(':startup-annotation')
    ksp project(":startup-ksp-compiler")
}

```

2. 创建一个任务并添加注解

```kotlin
@StartupGroup(group = "ksp")
class AsyncTask1 : SimpleStartupTask() {

    override fun mainThread(): Boolean {
        return false
    }

    override fun await(): Boolean {
        return true
    }

    override fun run(context: Context) {
        info("AsyncTask1")
    }
}
```

考虑到多进程，每个进程启动的任务不同的情况下需要对任务进行额外分组，下面demo为指定web进程的任务。获取注解方式大部分基于反射实现，属于合规的放心使用。

```kotlin
@StartupGroup(group = "ksp", strategy = Process.OTHER, processName = ["web"])
class SimpleTask3 : SimpleStartupTask() {

    override fun run(context: Context) {
        info("SimpleTask3")
    }
}

addMainProcTaskGroup { StartupTaskGroupApplicationKspAll() }
```

指定主进程任务

```kotlin
@StartupGroup(group = "ksp", strategy = Process.MAIN)
class SimpleTask2 : SimpleStartupTask() {

    override fun run(context: Context) {
        info("SimpleTask2")
    }

}
```

3. 添加组，该产物会存在在`build/generated/ksp`文件路径下，并根据group和moduleName等作为唯一类名。

```kotlin
public class StartupTaskGroupApplicationKspAll : StartupTaskGroup {
    public override fun group(): MutableList<StartupTask> {
        val list = mutableListOf<StartupTask>()
        list.add(AsyncTask1())
        list.add(SimpleTask1())
        return list
    }
}

addTaskGroup(StartupTaskGroupApplicationKspAll())
```

主进程任务组

```kotlin
public class StartupTaskGroupApplicationKspMain : StartupTaskGroup {
    public override fun group(): MutableList<StartupTask> {
        val list = mutableListOf<StartupTask>()
        list.add(SimpleTask2())
        return list
    }
}

```

子进程taskGroup

```kotlin
public class StartupProcTaskGroupApplicationKsp : StartupTaskProcessGroup {
    public override fun group(process: String): MutableList<StartupTask> {
        val list = mutableListOf<StartupTask>()
        if (process.contains("web")) {
            list.add(SimpleTask3())
        }
        return list
    }
}

```

# dsl

通过dsl形式动态添加task

构造startup builder ，之后添加任务。 这部分主要就是因为需要构造较多的task函数，看起来不立体了。

以下是dsl版本参考，能力是将简单的任务可以通过dsl的形式添加进去。

```kotlin
fun Application.createStartup(): Startup.Builder = run {
    startUp(this) {
        addTask {
            simpleTask("taskA") {
                info("taskA")
            }
        }
        addTask {
            simpleTask("taskB") {
                info("taskB")
            }
        }
        addTask {
            simpleTask("taskC") {
                info("taskC")
            }
        }
        addTask {
            simpleTaskBuilder("taskD") {
                info("taskD")
            }.apply {
                dependOn("taskC")
            }.build()
        }
        addTask("taskC") {
            info("taskC")
        }
        setAnchorTask {
            MyAnchorTask()
        }
        addTask {
            asyncTask("asyncTaskA", {
                info("asyncTaskA")
            }, {
                dependOn("asyncTaskD")
            })
        }
        addAnchorTask {
            asyncTask("asyncTaskB", {
                info("asyncTaskB")
            }, {
                dependOn("asyncTaskA")
                await = true
            })
        }
        addAnchorTask {
            asyncTaskBuilder("asyncTaskC") {
                info("asyncTaskC")
                sleep(1000)
            }.apply {
                await = true
                dependOn("asyncTaskE")
            }.build()
        }
        addAnchorTask {
            asyncTask("asyncTaskD") {
                info("asyncTaskD")
                sleep(1000)
            }
        }
        addAnchorTask {
            asyncTask("asyncTaskE") {
                info("asyncTaskE")
                sleep(10000)
            }
        }
        addTaskGroup { taskGroup() }
        addTaskGroup { StartupTaskGroupApplicationKspMain() }
        addMainProcTaskGroup { StartupTaskGroupApplicationKspAll() }
        addProcTaskGroup { StartupProcTaskGroupApplicationKsp() }
    }
}
```

# 锚点任务组

等项目稳定之后，会设立几个锚点任务，后续任务只要挂载到锚点任务之后执行即可，可以简单的设立任务基准。

1.添加锚点任务

```kotlin
buildAnchorTask {
    MyAnchorTask()
}
```

2. 挂载mustAfter 

```kotlin
 mustAfterAnchor {
            asyncTask("asyncTaskE") {
                info("asyncTaskE")
                sleep(10000)
            }
        }
```

# 调试能力

通过提供额外的调试工程，当启动的所有任务完成后会自动跳转页面并渲染结果，无需任何注册逻辑。

## 使用

```gradle
dependencies {
    debugImplementation project(':startup-dag-view')
}
```

> 小贴士 不想要该功能则只需 在values下配置如下

```xml

<bool name="startup_install_provider_enable">false</bool>
```

# ksp

通过ksp 生成一部分动态的组，这部分暂时只能手动添加，后续考虑优先梳理依赖，但是并没有完全完成解耦。

# TODO

任务防止劣化的调试工具没有完成。

# 玩具属性

因为是玩具工程，仅供大家学习了，有问题可以直接联系我，联系方式如下

> qq: 454327998
