-> JVM 21 - G1 region pinning reproduces
-> JVM 25 - fixed!

Tested with ~24 cores

Validate that it's happening/hanging a lot  when running with:

`tail -F logs/gc-backend.log`