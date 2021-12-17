import os
import multiprocessing
import re

exp = re.compile(r'[\+,\-]?[Queue]{1,}[0-9]|[0-9]{1,3}')

def worker(Ceuri, arg2, arg3):
        #print (Ceuri)
        #print (arg2)
        #print (arg3)
        cmd = 'cmd /k "cd C:\\Users\\diana\\git\\Sistem_birocratic_PCBE\\pcbe &'  +"ant MainApp -Darg1=" +Ceuri +" -Darg2= "+arg2+" -Darg3="+arg3 
        os.system(cmd)

if __name__ == '__main__':
    
   intList = []
   Queue_List  = []
   jobs = []
   DeskList=[]
   NOfEmp=[]
   NOfDesks = 0
   
   with open(r'C:\Users\diana\Desktop\test.txt','r') as file:
       lines = file.readlines()
       for line in lines:
           out = re.findall(exp,line.strip())
           for i in out:
               if i.isdigit() == True:
                   intList.append(i)
                   #print (i)
               else:
                   Queue_List.append(i)
                   #print (i)

   NOfDesks= intList[0]
   for i in range(1,int (NOfDesks)*2+1):
       if i%2==0:
           NOfEmp.append(intList[i])
       else:
           DeskList.append(intList[i])
   for i in range( int(NOfDesks) ):
        p = multiprocessing.Process(target=worker, args =(Queue_List[i],DeskList[i],NOfEmp[i],))
        jobs.append(p)
        p.start()
       
          

