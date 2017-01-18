#!/usr/bin/python3
from flask import Flask, request
from flask_restful import Resource, Api
from multiprocessing.dummy import Pool
import sys
import requests

app = Flask(__name__)
api = Api(app)
pool = Pool(10)

#def async_req(dsts):
#    print ('async_req: %s' % dsts)
#    , {'dst':'|'.join(dsts[1:])}


class Var(Resource):
    def put(self):
        if any(request.form):
            dsts = list(request.form)[0].split('|')
            #pool.apply_async(requests.put, [dsts[0]], {'dst':'|'.join(dsts[1:])})
            req = requests.put(dsts[0], data='|'.join(dsts[1:]))
            print(dsts[0])
            return "[%s, %s ] | "  %  (dsts, req.text)
        else:
            print("END OF THE LINE")
            return "THE END"
        return None
        
api.add_resource(Var, '/')

if __name__ == '__main__':
    if len(sys.argv) != 2:
        print('Usage: <port>')
        exit(1)
    else:
        app.run(debug=False, host='0.0.0.0', port=int(sys.argv[1]))
