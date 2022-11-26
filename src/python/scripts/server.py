import base64
from hashlib import sha1
import socket
import ssl
import os
import time
import sqlite3

import uuid



import conf

host_ip, server_port = "127.0.0.1", 9999

class ssl_server():
    def server(self):

        
            server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)         
            server.bind((host_ip,server_port))
            print(f"server running at '{host_ip}' with '{server_port}'\n ready to accept requests")
            server.listen(3)
            while True:
                conn, add = server.accept()
                print(f"server trying to connec {add}")
                while True:
                    data = conn.recv(1024).strip().decode()
                    print(data)
                    # <----- * -----> data | empoyeeId | Sign 
                    employeeId, query, sign = data.split("|")
                    
                    # <----- * ----->
                    credentials = list()
                    if os.path.exists(conf.DB):
                        con = sqlite3.connect(conf.DB)
                        cur = con.cursor().execute('SELECT * from credentials')
                        # Obtain all the credentials from the database [(employeeId, pki),(employeeId, pki)]
                        credentials = cur.fetchall()
                        if len(credentials)!=0:
                            for entry in credentials:
                                if entry[0][0] == employeeId :
                                    # <----- * -----> COMPLETAR CON VERIFICACION DE FIRMA
                                    verificacion = 0
                                    # <----- * ----->
                                    timeStamp = time.strftime("[%d/%m/%y %H:%M:%S]", time.localtime())
                                    deliveryId = uuid.uuid1()
                                    cur.execute(f"INSERT INTO deliveries(deliveryId, timeStamp, data, verification) VALUES ('{deliveryId}','{timeStamp}','{query}','{verificacion}')")
                                    con.commit()
                                    con.close()


    def log(self,message,display=False):
            """
            Returns by console and writes the log in the log file.
            """
            if display: print(message)        
            with open(conf.LOG, 'a') as f:
                f.write("\n"+message)
                f.close()

     
if __name__ == "__main__":
    ssl_server().server()