# Documentation for API of TSL tournament manager

This is a simple guide for the API given in this website, where all data given and received will be in JSON format. To access most of the api you will need to POST the username and password to the /Login/ url to gain a token to access the rest of the api.


## HTTP Methods

For each model the corresponding url (i.e /Users/, /Tournaments/, /Captains/, . . . ) will have all Methods options available.

### POST, GET, DELETE (For the whole models)
  These are the method options for the Models as a whole which can be used at the base url of the models mentioned earlier. 

  * POST: Must POST within closed square brackets [ ] for multiple or singular object POSTs and inputted fields must match the model fields in gsl_fantasy_tournament/tournament/models.py.

  * GET: Data will be received within [ ] containing all objects of the given model.

  * DELETE: Will delete the entire set of objects stored immediately.
    
### PUT, GET, DELETE (For specific objects in models)
  these are the method options for the specific objects in the Models which can be used at the base url with a key number after to access that object of that key. ie, /Players/4 , this accessing the 4th player selected.

  * PUT: No square bracketed [ ] when giving the the fields, which again must be matching the model fields gsl_fantasy_tournament/tournament/models.py.

  * GET: Data will be received without [ ] and only the object with the corresponding key.

  * DELETE: Will delete just the key specified object.



