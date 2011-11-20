import sys,re

try:
    filename = sys.argv[1]
    file = open(filename, 'r')
    newfile = open(sys.argv[2] , 'w')
except:
    print "meh"


comment = r'#.*'

for line in file:
    print line
    newfile.write(re.sub(comment,'', line))

file.close()
newfile.close()
        
        
