import sys,re

try:
    filename = sys.argv[1]
    file = open(filename, 'r')
    newfile = open(sys.argv[2] , 'w')
except:
    print "Usage : python removecomments.py infile outfile"


comment = r'#.*'

for line in file:
    newfile.write(re.sub(comment,'', line))

file.close()
newfile.close()
        
        
