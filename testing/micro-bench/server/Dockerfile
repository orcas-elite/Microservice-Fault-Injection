FROM python:3.5-alpine
RUN pip3 install flask
COPY app.py /app/app.py
WORKDIR /app
RUN pip install flask
ENTRYPOINT ["python"]
CMD ["app.py"]
