import random
import string

from flask import Flask, jsonify

app = Flask(__name__)


@app.route('/')
def root():
    return jsonify(result=''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(2 ** 10)))


if __name__ == '__main__':
    app.run(debug=False, host='0.0.0.0')
