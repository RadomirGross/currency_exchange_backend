# Используем официальный образ Tomcat
FROM tomcat:10.1.33-jre21

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /usr/local/tomcat/webapps

# Копируем ROOT.war в папку webapps
COPY ROOT.war /usr/local/tomcat/webapps/

# Указываем, что контейнер будет слушать порт 8080
EXPOSE 8080

# Запускаем Tomcat
CMD ["catalina.sh", "run"]