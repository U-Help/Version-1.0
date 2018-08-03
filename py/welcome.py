from flask import Flask, request
import mysql.connector

app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'hello world'

@app.route('/login',methods=['POST'])
def login():
    print(request.json)
    conn=mysql.connector.connect(host='localhost',port='3306',user='android',password='666',database='android')
    cursor=conn.cursor(buffered=True)
    cursor.execute("select password from user where username='%s'" %(request.json["username"]))
    result=cursor.fetchall()
    cursor.close()
    conn.close()
    if(len(result)==1 and result[0][0]==request.json["password"]):
      print("Login Successfully")
      return "success"
    else:
      print("Login Failed")
      return "fail"
      
@app.route('/register', methods=['POST'])
def register():
    print(request.json)
    conn=mysql.connector.connect(host='localhost',port='3306',user='android',password='666',database='android')
    cursor=conn.cursor(buffered=True)
    try:
      cursor.execute("insert into user values('%s','%s')" %(request.json["username"],request.json["password"]))
    except Exception:
      print("Register Failed: Exception Occur")
      cursor.close()
      conn.close()
      return "User Exist"
    else:
      print("Register Successfully")
      conn.commit()
      cursor.close() 
      conn.close()
      return "welcome"

if __name__ == '__main__':
    app.run(host='0.0.0.0')
