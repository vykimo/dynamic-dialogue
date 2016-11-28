'''
To create a R&R ranker manually, we need a csv file containing the
query, the feature and the rankink

The export builds a json document with all the questions and the selected answers
and their ranking from 1 to 4
'''
import json
import sys, getopt
import uuid

index=1
mapIdx={}
#
def usage():
    print('TransformQuestionExportTocsb.py -i <question export file> -u <user of the bluemix account>')

def mapUidToInt(id):
    global index
    if id not in mapIdx:
        mapIdx[id]=index
        index+=1
    return mapIdx[id]

def cleanQuestion(qtext):
    charToRemove=[',',';',':']
    return ''.join([c for c in qtext if c not in charToRemove])

# The question lists the answer units with ranking as defined by the user
# it may be possible that some documents were removed, so the ranking should not be considered
# return a string with multiple lines: one for each answer, ranking
def cleanAnswers(question,user):
    cleanAnswers=''
    for a in question['cluster']['answers'][user]:
        if a['source']['file'] != 'Batteries-MaximizingPerformance-Apple.html':
            uid=uuid.uuid1()
            cleanAnswers+=str(uid)+","
            cleanAnswers+=str(mapUidToInt(a['id']))+","
            cleanAnswers+=str(a['ranking'])+"\n"
    return cleanAnswers

def main(argv):
    USER="boyerje@us.ibm.com"
    FILE="./data/export-questions.json"
    try:
        opts, args = getopt.getopt(argv,"hi:u:",["inputfile=","user="])
    except getopt.GetoptError:
        print(usage())
        sys.exit(2)

 
    for opt, arg in opts:
        if opt == '-h':
            usage()
            sys.exit()
        elif opt in ("-u", "--user"):
            USER = arg
        elif opt in ("-i", "--inputfile"):
            FILE = arg
    print('############## Transform Retrieve and Ranking Question Export #######')
    f=open(FILE,'r')
    g=open('training-set.csv','w')
    g.write('query_id,answer_unit,ground_truth\n')
    questions=json.load(f)
    for q in questions:
        answersString=cleanAnswers(q,USER)
        g.write(answersString)
    f.close()
    g.close()
    print('#### Done ! file training-set.csv created')
        
if __name__ == "__main__":
    main(sys.argv[1:])
   
