import sqlite3
import conf

con = sqlite3.connect(conf.DB)

cur = con.cursor()

cur.execute('SELECT * from credentials')


ls = cur.fetchall()

print(ls[0][0])