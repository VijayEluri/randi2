log4j.rootLogger=OFF

# Allgemeines Debug Log
log4j.logger.de.randi2=ALL, DebugTXT

# TODO: Auf der Konsole werden die folgenden Infos angezeigt
# Jenachdem woran gearbeitet wird, sollten hier die Klassen/
# Packages angepasst werden
#log4j.logger.de.randi2.utility=ALL, Konsole, ArbeitDebugTXT
log4j.logger.de.randi2.datenbank=ALL, Konsole, ArbeitDebugTXT

# Anwendungslog
log4j.logger.Randi2=ALL, AnwendungXML, Konsole

#Proxoollog
log4j.logger.org.logicalcobwebs.proxool.randi2 =ALL, Proxool


######################################################
log4j.appender.AnwendungXML=org.apache.log4j.DailyRollingFileAppender
log4j.appender.AnwendungXML.datePattern='.'yyyy-MM-dd
log4j.appender.AnwendungXML.layout=de.randi2.utility.LogLayout

log4j.appender.Konsole=org.apache.log4j.ConsoleAppender
log4j.appender.Konsole.layout=org.apache.log4j.PatternLayout
log4j.appender.Konsole.layout.ConversionPattern=[%t] %d{ISO8601} [%p] %c:\n  %m%n

log4j.appender.DebugTXT=org.apache.log4j.RollingFileAppender
log4j.appender.DebugTXT.MaxFileSize=100KB
log4j.appender.DebugTXT.layout=org.apache.log4j.PatternLayout
log4j.appender.DebugTXT.layout.ConversionPattern=[%t] %d{ISO8601} [%p] %c:  %m%n

log4j.appender.ArbeitDebugTXT=org.apache.log4j.RollingFileAppender
log4j.appender.ArbeitDebugTXT.MaxFileSize=100KB
log4j.appender.ArbeitDebugTXT.layout=org.apache.log4j.PatternLayout
log4j.appender.ArbeitDebugTXT.layout.ConversionPattern=[%t] %d{ISO8601} [%p] %c:  %m%n

log4j.appender.Proxool=org.apache.log4j.DailyRollingFileAppender
log4j.appender.Proxool.datePattern='.'yyyy-MM-dd
log4j.appender.Proxool.layout=org.apache.log4j.PatternLayout
log4j.appender.Proxool.layout.ConversionPattern=%d{DATE} [%-5p] %c {%F:%L} - %m%n

### TODO: Dateipfade zum Tomcat logs Verzeichnis anpassen ##
log4j.appender.AnwendungXML.File=randi2_logs/anwendung_log.xml
log4j.appender.DebugTXT.File=randi2_logs/entwicklung_allgemein.log
log4j.appender.ArbeitDebugTXT.File=randi2_logs/entwicklung_arbeit.log
log4j.appender.Proxool.File=randi2_logs/proxool_log.log