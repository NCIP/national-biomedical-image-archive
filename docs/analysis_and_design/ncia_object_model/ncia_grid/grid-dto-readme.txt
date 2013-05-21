In this directory you should find the EA project ncia_grid_dto_simple.EAP and
another accompanying XML model interchange file (.xmi) that is a working 
export of that project for use by Introduce when creating or modifying the 
"node-to-node" grid implementation for the NCIA.  

This represents 1 of the 3 different object models that are required to build
and deploy the NCIA in it's entirety. This model contains the data types that
 represent the DTOs that are used for over the grid NCIA "node-to-node".

The other two models represent a) the model that is used to generate our caCORE SDK
backend that we use to communicate with our data source, and b) our official "DICOM" model 
that is used to generate our grid facing services that are not utilized by the NCIA, but by external
entities.  

The "DICOM" model is a registered Object Model and is used for our BDT and 
Generic Grid data service.  



 