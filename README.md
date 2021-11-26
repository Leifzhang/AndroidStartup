# AndroidStartUp

  玩具工程

  想优化下项目内的启动框架，还是基于有向无环图将任务排序。之后拆分多线程等待执行任务。

# 依赖tag化

剔除了传统的类形式的依赖方式，转化成tag，标识当前任务的依赖。

好处就是多模块解耦，劣势就是更加抽象，并不能形成立体的依赖关系。

# task分组

只是增加了个简单的组的概念而已，内部的依赖顺序还是要重新调整

# dsl

通过dsl形式动态添加task


构造startup builder ，之后添加任务。 这部分主要就是因为需要构造较多的task函数，看起来不立体了。

```kotlin
 startUp(this) {
    addTask("taskA") {
        info("taskA")
    }
    addTask({
        info("taskD")
    }, {
        tag = "taskD"
        dependOn("taskC")
    })
}
```

# ksp

通过ksp 生成一部分动态的组，这部分暂时只能手动添加，后续考虑优先梳理依赖，但是并没有完全完成解耦。


# 玩具属性

因为是玩具工程，仅供大家学习了
 
