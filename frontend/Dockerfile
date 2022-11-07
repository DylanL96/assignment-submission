# first line starts with a FROM message
# going to be building this Docker container from an image
# an image is a saved piece of state that represents the blueprint for the container
# instantiate containers into existence based upon the image blueprint
# we are going to start from this node 17 version of alpine linux
FROM node:17-alpine3.15 

# going to need to copy data from our machine onto this container
# so we are going to copy files from our source code in the frontend folder
# this is the format -> COPY [host machine] [container] (first path is relative to the Dockerfile, second is relative to the root of the container)
COPY package.json package-lock.json ./

# if we copy package.json and package-lock.json and we're sitting with a node enabled container
# we have to do the command RUN npm install
RUN npm install

COPY . .

RUN npm run build

# test this with terminal on mac
# the . means look at this current directory
# run the command docker build .