@echo off
d:
if exist studio.sql goto restaura
choice /c:123
if errorlevel 3 goto opcao3 
if errorlevel 2 goto opcao2 
if errorlevel 1 goto opcao1 
:opcao1 
echo. 
echo Você apertou a tecla 1 
goto fim 
:opcao2 
echo. 
echo Você apertou a tecla 2 
goto fim 
:opcao3 
echo. 
echo Você apertou a tecla 3 
goto fim 

:restaura
c:
cd \
cd "Program Files (x86)"
cd MySQL\MySQL Server 5.1\bin
dir /p
mysql -uroot -padmin -h localhost studio < D:\studio.sql

:tela 
@echo "MSG * alertas"
@echo "cd %PROGRAMFILES%\MySQL\MySQL Server 5.1\bin"
@echo "mysql -uroot -padmin -h localhost studio < D:\studioVazio.sql"
goto fim

:fim
pause