import sys,re

try:
    filename = sys.argv[1]
except:
    print "no file found"

file = open(filename, 'r')
newfile = open("mod_"+filename , 'w')
test = r'\s*\d+.*'
for line in file:
    if (not re.match(test,line)):
        newfile.write(line)
newfile.close
file.close
