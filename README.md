# EvAlluate: Example modules

This is a repository containing example module implementations for [the EvAlluate application](https://github.com/kv70416/evalluate).

Built libraries of these modules are included in release builds of the main EvAlluate application.

Currently implemented examples include:
- **Local File Fetching Module**, a *File Fetching Module* which retrieves solutions from a local directory
- **GitLab Fetching Module**, a *File Fetching Module* which uses GitLab API to retrieve forks of a base repository as solutions
- **Isolate C Module**, a *Code Compilation Module* designed to set-up an [isolate sandbox](https://github.com/ioi/isolate) environment and compile and execute C code via a GCC compiler.
  - ***Note***: The isolate software and GCC compiler are not included in the implementation, and should be installed separately from their respective sources. 
- **Isolate Java Module**, a *Code Compilation Module* designed to set-up an [isolate sandbox](https://github.com/ioi/isolate) environment and compile and execute Java code via a JDK.
  - ***Note***: The isolate software and JDK are not included in the implementation, and should be installed separately from their respective sources. 
- **Isolate Python Module**, a *Code Compilation Module* designed to set-up an [isolate sandbox](https://github.com/ioi/isolate) environment and execute Python code via a Python interpreter.
  - ***Note***: The isolate software and Python interpreter are not included in the implementation, and should be installed separately from their respective sources.
- **Uniform Test Case Scoring Module**, a *Solution Scoring Module* for scoring the solutions on a per-test-case basis, with each test case awarding a set amount of points.
- **Levenshtein Distance Module**, a *Duplicate Detection Module* for rating the autheticity of solutions via an algorithm based on Levenshtein distance calculation.
- **PDF Result Export Module**, a simple *Result Export Module* for exporting the outcomes of the evaluation process to a PDF file.
