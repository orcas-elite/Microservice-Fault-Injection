FROM undera/taurus
ADD cfg.yml /lgen/cfg.yml
ADD run.sh /lgen/run.sh
RUN rm /etc/bzt.d/90-artifacts-dir.json
EXPOSE 8000
ENTRYPOINT ["sh", "/lgen/run.sh"]