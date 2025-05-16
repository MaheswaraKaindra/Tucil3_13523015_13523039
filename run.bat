@echo off
setlocal enabledelayedexpansion

set JFX_LIB=lib\win
set JFX_NATIVE=lib\win\native
set SRC_DIR=src\main\java
set RES_DIR=src\main\resources
set OUT_DIR=bin\main

mkdir %OUT_DIR% 2>nul

javac --module-path %JFX_LIB% --add-modules javafx.controls,javafx.fxml ^
      -d %OUT_DIR% ^
      %SRC_DIR%\app\App.java

if errorlevel 1 (
    exit /b 1
)

xcopy /Y %RES_DIR%\layout.fxml %OUT_DIR%\ >nul

java --module-path %JFX_LIB% --add-modules javafx.controls,javafx.fxml ^
     -Djava.library.path=%JFX_NATIVE% ^
     -cp %OUT_DIR% app.App

endlocal
pause
