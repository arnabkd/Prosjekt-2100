CLess.jar: ALWAYS
	(cd no/uio/ifi/cless/cless; make)
	jar cmf manifest.txt CLess.jar no/uio/ifi/cless/*/*.class

clean:
	rm -f *.jar *~
	for D in no/uio/ifi/cless/*; do (cd $$D; make clean); done

ALWAYS:
