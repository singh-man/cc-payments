### For detailed requirements go through

REQUIREMENT.md

### 1. To Build

```
docker build --rm -t <image-name> .
```

### 2. To RUN

```
docker run --rm -v <path-to-data-file>/data:/root/data -p 8080:8080 <image-name-as-per-previous-step>
```
### Result
The requisite payment needed is printed on the Terminal as detailed in REQUIREMENT.md

### Customer end points exposed
1. GET -> http://localhost:8080/customer/all -> lists all available customers in the system.
2. GET -> http://localhost:8080/customer/{name} -> get the details of individual customer.

### Run it in IDE
- Clone or download as zip
- Follow the guidelines of the IDE to import the existing maven project.
- Either allow IDE to build to do ```mvn clean install```