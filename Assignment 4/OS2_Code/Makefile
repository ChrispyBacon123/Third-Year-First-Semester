#Christopher Blignaut
#BLGCHR003

JAVAC=/usr/bin/javac
JAVA=/usr/bin/java
SRCDIR=src
BINDIR=bin
.SUFFIXES: .java .class

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) -sourcepath $(SRCDIR) $<

# Getting Pathway to main 
PACKAGE = barScheduling
CLASSES= ThroughputThread.class DrinkOrder.class Barman.class Patron.class SchedulingSimulation.class
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/$(PACKAGE)/%.class)
MAIN = SchedulingSimulation

# Argument to be passed in to program
args = 100 0

# Base argument -- nothing to be done
default: $(CLASS_FILES)

# Clean command
clean:
	rm $(BINDIR)/* .class

# Run command for a single instance
run: $(CLASS_FILES)
	$(JAVA) -cp $(BINDIR) $(PACKAGE).$(MAIN) $(args)