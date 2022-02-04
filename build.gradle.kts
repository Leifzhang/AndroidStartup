// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra["kotlin_version"] = "1.5.30"

    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/central/") }
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/central/") }
        google()
    }
    configurations.all {
        resolutionStrategy.dependencySubstitution.all {
            if (requested is ModuleComponentSelector) {
                val moduleRequested = requested as ModuleComponentSelector
                val p = rootProject.allprojects.find { p ->
                    (p.group == moduleRequested.group && p.name == moduleRequested.module)
                }
                if (p != null) {
                    useTarget(project(p.path), "selected local project")
                }

            }
        }
    }

    // 项目内如果想要引用到生成的代码 使用下列方式
    afterEvaluate {
        extensions.findByType(org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension::class)
            ?.apply {
                sourceSets.forEach {
                    it.kotlin.srcDir("build/generated/ksp")
                }
            }
    }



    group = "com.kronos.startup"
}


// 耗时统计kt化
class TimingsListener : TaskExecutionListener, BuildListener {
    private var startTime: Long = 0L
    private var timings = linkedMapOf<String, Long>()


    override fun beforeExecute(task: Task) {
        startTime = System.nanoTime()
    }

    override fun afterExecute(task: Task, state: TaskState) {
        val ms = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)
        task.path
        timings[task.path] = ms
        project.logger.warn("${task.path} took ${ms}ms")
    }

    override fun buildFinished(result: BuildResult) {
        project.logger.warn("Task timings:")
        timings.forEach {
            if (it.value >= 50) {
                project.logger.warn("${it.key} cos  ms  ${it.value}\n")
            }
        }
    }


    override fun settingsEvaluated(settings: Settings) {
    }

    override fun projectsLoaded(gradle: Gradle) {

    }

    override fun projectsEvaluated(gradle: Gradle) {

    }

}

gradle.addListener(TimingsListener())

/*
task clean(type: Delete) {
    delete rootProject.buildDir
}*/
