rootProject.name = "debug-demo"
include("backend", "frontend", "load-gen",
    // separate demo cases
    "backend-gc-issue",
    "backend-cpu-issues"
)
