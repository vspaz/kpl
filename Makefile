TARGET=kpl

build:
	mvn clean compile assembly:single

run:
	java -jar $(TARGET).jar

clean:
	rm -f $(TARGET).jar
