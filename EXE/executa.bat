@echo off
TITLE Executant Projecte PROP
CLS

:: 1. Ens posem a la carpeta on esta aquest script (EXE)
cd /d "%~dp0"

:: 2. Sortim de EXE i entrem a FONTS
cd ..\FONTS

:: 3. Verifiquem si som al lloc correcte
IF NOT EXIST gradlew.bat (
    ECHO [ERROR] No trobo 'gradlew.bat' dins de la carpeta FONTS.
    ECHO Revisa que l'estructura sigui: projecte/FONTS/gradlew.bat
    PAUSE
    EXIT /B
)

:: 4. Executem
ECHO [INFO] Executant des de FONTS...
call gradlew.bat run

:: 5. Pausa final per veure errors
IF %ERRORLEVEL% NEQ 0 (
    ECHO.
    ECHO [ERROR] El programa ha fallat.
)
PAUSE