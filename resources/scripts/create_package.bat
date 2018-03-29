:: Create folder jars in root project space and copy this script to that folder

MKDIR data
MKDIR data\drivers
COPY ..\data\drivers\*.* data\drivers
"C:\Program Files\7-Zip\7z.exe" a -tzip BBScoutHelper.zip *.jar data