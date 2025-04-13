@echo off
REM Nom du fichier WAR
set WAR_NAME=avion.war

REM Chemins importants
set SRC_DIR=src
set BUILD_DIR=build
set WEBAPP_DIR=webapp
set LIB_DIR=%WEBAPP_DIR%\WEB-INF\lib
set TEMP_WAR_DIR=war_temp
set TEMP_SRC_DIR=src_temp

REM Chemin vers le dossier de deploiement de tomcat
set WILDFLY_DEPLOY_DIR=C:\software\apache-tomcat-9.0.104\webapps

REM Verifie si le dossier build existe, sinon le cree
if not exist "%BUILD_DIR%" (
    mkdir "%BUILD_DIR%"
)

REM Supprimer le dossier temporaire source precedent s'il existe
if exist "%TEMP_SRC_DIR%" (
    rmdir /s /q "%TEMP_SRC_DIR%"
)

REM Creer un dossier temporaire pour les fichiers Java
mkdir "%TEMP_SRC_DIR%"

REM Copier uniquement les fichiers .java dans le dossier temporaire
echo Copie des fichiers .java vers le dossier temporaire...
for /r "%SRC_DIR%" %%f in (*.java) do (
    copy "%%f" "%TEMP_SRC_DIR%" > nul
)
if %ERRORLEVEL% neq 0 (
    echo Erreur lors de la copie des fichiers .java. Arrêt du script.
    exit /b 1
)

REM Compilation des fichiers .java
echo Compilation des fichiers .java...
set CLASSPATH=
for %%j in ("%LIB_DIR%\*.jar") do (
    set CLASSPATH=!CLASSPATH!;%%j
)

REM Activer l'expansion retardee
setlocal enabledelayedexpansion

REM Construire une liste des fichiers Java pour javac
set JAVA_FILES=
for %%f in ("%TEMP_SRC_DIR%\*.java") do (
    set JAVA_FILES=!JAVA_FILES! "%%f"
)

REM Lancer la compilation
javac -parameters -d "%BUILD_DIR%" -cp "%LIB_DIR%\*" !JAVA_FILES!
if %ERRORLEVEL% neq 0 (
    echo Erreur lors de la compilation. Arrêt du script.
    endlocal
    exit /b 1
)
endlocal
echo Compilation reussie.

REM Supprimer le dossier temporaire source
rmdir /s /q "%TEMP_SRC_DIR%"

REM Supprimer le dossier temporaire precedent s'il existe
if exist "%TEMP_WAR_DIR%" (
    rmdir /s /q "%TEMP_WAR_DIR%"
)

REM Creer le dossier temporaire pour l'assemblage du WAR
mkdir "%TEMP_WAR_DIR%"

REM Copier les fichiers compiles (classes) dans le dossier temporaire
xcopy "%BUILD_DIR%" "%TEMP_WAR_DIR%\WEB-INF\classes" /E /I /Q


REM Copier le contenu de WEBAPP (fichiers JSP, HTML, etc.) dans le dossier temporaire
xcopy "%WEBAPP_DIR%" "%TEMP_WAR_DIR%" /E /I /Q

REM Creer le fichier WAR a partir du dossier temporaire
echo Creation du fichier WAR...
cd "%TEMP_WAR_DIR%"
jar -cvf "../%WAR_NAME%" *
cd ..

REM Supprimer le dossier temporaire
rmdir /s /q "%TEMP_WAR_DIR%"

REM Verifier si le fichier WAR a ete cree avec succes
if exist "%WAR_NAME%" (
    echo Fichier WAR cree avec succes : %WAR_NAME%
) else (
    echo Erreur lors de la creation du fichier WAR.
    exit /b 1
)

REM Copier le fichier WAR dans le dossier de deploiement de WildFly
if exist "%WILDFLY_DEPLOY_DIR%" (
    echo Copie du fichier WAR vers Tomcat...
    copy "%WAR_NAME%" "%WILDFLY_DEPLOY_DIR%\" > nul
    if %ERRORLEVEL% neq 0 (
        echo Erreur lors de la copie vers Tomcat.
        exit /b 1
    ) else (
        echo Deploiement vers Tomcat reussi.
    )
) else (
    echo Dossier de deploiement de Tomcat introuvable : %WILDFLY_DEPLOY_DIR%
    exit /b 1
)
