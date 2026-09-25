rootProject.name = "debug-demo"
include("backend", "proxy", "load-gen",
    // separate demo cases
    "backend-gc-issue",
    "backend-cpu-issues"
)
