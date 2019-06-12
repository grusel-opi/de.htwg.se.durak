###
# 1) give access to X server: 
# $ xhost +
#
# 2) run:
# $ sudo docker run -v /tmp/.X11-unix:/tmp/.X11-unix -e DISPLAY=unix$DISPLAY gruselopi/de.htwg.se.durak:v1
#
# 3) disable x server access:
# $ xhost -
#
###

FROM hseeberger/scala-sbt
RUN apt-get update && apt-get install -y --no-install-recommends openjfx && rm -rf /var/lib/apt/lists/*
RUN export CLASSPATH=$CLASSPATH:/usr/share/java/openjfx/lib/
WORKDIR /durak
ADD . /durak
CMD sbt test && sbt run


