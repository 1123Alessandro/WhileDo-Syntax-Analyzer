cd classes
del *.class /S
cd ..
cd src
javac -d ../classes/ main.java
cd ../classes
cls
java Main
@echo off
cd ..
