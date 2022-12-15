JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	GWackClientGUI.java \
	GWackChannel.java \

all: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
