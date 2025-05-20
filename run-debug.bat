@echo off
setlocal

set JFX_LIB=lib\win
set JFX_NATIVE=lib\win\native
set SRC_DIR=src\main\java
set RES_DIR=src\main\resources
set OUT_DIR=bin\main

if not exist %OUT_DIR% (
    mkdir %OUT_DIR%
)

for /r %SRC_DIR% %%f in (*.java) do (
    echo %%f
) > sources.txt

javac --module-path %JFX_LIB% --add-modules javafx.controls,javafx.fxml ^
      -d %OUT_DIR% @sources.txt >nul 2>&1
del sources.txt

for /r %RES_DIR% %%f in (*.fxml) do (
    copy /Y "%%f" %OUT_DIR% >nul
)

java --module-path %JFX_LIB% --add-modules javafx.controls,javafx.fxml ^
     -Djava.library.path=%JFX_NATIVE% ^
     -cp %OUT_DIR% app.App

endlocal
