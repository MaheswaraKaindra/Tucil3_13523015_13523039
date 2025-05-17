@echo off
setlocal enabledelayedexpansion

set JFX_LIB=lib\win
set JFX_NATIVE=lib\win\native
set SRC_DIR=src\main\java
set RES_DIR=src\main\resources
set OUT_DIR=bin\main

mkdir %OUT_DIR% 2>nul

javac --module-path %JFX_LIB% --add-modules javafx.controls,javafx.fxml -sourcepath %SRC_DIR% ^
      -d %OUT_DIR% ^
      %SRC_DIR%\app\RushHourApp.java %SRC_DIR%\app\components\*.java %SRC_DIR%\app\utils\*.java %SRC_DIR%\app\controller\*.java %SRC_DIR%\app\solver\*.java

if errorlevel 1 (
    exit /b 1
)

xcopy /Y %RES_DIR%\RushHourView.fxml %OUT_DIR%\ >nul
xcopy /Y %RES_DIR%\styles.css %OUT_DIR%\ >nul

java --module-path %JFX_LIB% --add-modules javafx.controls,javafx.fxml ^
     -Djava.library.path=%JFX_NATIVE% ^
     -cp %OUT_DIR% app.RushHourApp

endlocal
pause
