@echo off
setlocal
echo ================================
echo Iniciando Microblogging App...
echo ================================

REM Paso 1: Verificar si Docker está corriendo
echo Verificando Docker...
docker info >nul 2>&1
IF ERRORLEVEL 1 (
    echo [ERROR] Docker no está corriendo. Por favor, iniciá Docker Desktop y volvé a intentar.
    pause
    exit /b
)

REM Paso 2: Levantar infraestructura con Docker Compose
echo Levantando Mongo, Redis y Kafka...
docker-compose up -d

REM Esperar unos segundos para que los servicios se levanten
echo Esperando a que los servicios arranquen...
timeout /t 10 >nul

REM Paso 3: Mostrar contenedores activos
echo Contenedores en ejecución:
docker ps

REM Paso 4: Ejecutar la app con Gradle Wrapper
echo ================================
echo Ejecutando la aplicación Spring Boot...
echo ================================
call gradlew bootRun --args='--spring.profiles.active=local'

REM Paso 5: Mostrar logs de la app si el contenedor existe (opcional si usás un contenedor Java, desactivado acá)
REM docker logs -f microblogging-app

echo ================================
echo Aplicacion finalizada o detenida.
echo ================================
pause
