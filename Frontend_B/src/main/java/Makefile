JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Main.java \
	UserLoginView.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	del Main.class
	del UserLoginView.class

run:
	java Main
