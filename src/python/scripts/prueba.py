import sqlite3
import conf
import uuid

con = sqlite3.connect(conf.DB,timeout=10)

cur = con.cursor()

cur.execute('SELECT * from credentials')

e = "pepe23"
r = "surma"
t = "klk"
s = "please"

#ur.execute(f"INSERT INTO deliveries(deliveryId, timeStamp, data, verification) VALUES ('{e}','{r}','{t}','{s}')")
con.commit()
ls = cur.fetchall()

for entrada in ls:
    print(entrada[1])
    if entrada[0][0] == '1':
        print("del tiron")

print(ls[0])
print(uuid.uuid1())
con.close()