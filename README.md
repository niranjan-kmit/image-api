# image-api
Image Api is simple rest full microservice which interacting with the imguru opensource rest api for 
uploading the images and updating the images et....

##### Register The User :
`curl --location 'http://localhost:8080/api/users/register' \
--header 'Content-Type: application/json' \
--data '{
"name": "user_one",
"password": "password!0",
"imageIds":[],
"error": ""
}'`

Sample Out Put : 

![img_1.png](img_1.png)

##### Upload Image :
`curl --location 'http://localhost:8080/api/image/upload' \
--header 'image_info: {"title":"Imgae Tile G","description":"Description for Imag"}' \
--header 'Authorization: Basic dXNlcl9vbmU6cGFzc3dvcmQhMA==' \
--form 'image=@"/C:/Users/Niranjan Nallapu/OneDrive/Desktop/cat.jfif"'`

Sample Out Put :

![img_2.png](img_2.png)

##### Get Image :
`curl --location 'http://localhost:8080/api/image/Yz5pYBO' \
--header 'Authorization: Basic dXNlcl9vbmU6cGFzc3dvcmQhMA==' \`

Sample Out Put :

![img_3.png](img_3.png)

##### Update Image :
`curl --location 'http://localhost:8080/api/image/Yz5pYBO' \
--header 'Authorization: Basic dXNlcl9vbmU6cGFzc3dvcmQhMA==' \
--form 'title="Image Title"' \
--form 'description="Image Description"'`

Sample Out Put :

![img_4.png](img_4.png)

##### Delete Image :
`curl --location --request DELETE 'http://localhost:8080/api/image/Yz5pYBO/delete' \
--header 'Authorization: Basic dXNlcl9vbmU6cGFzc3dvcmQhMA=='`

Sample Out Put :

![img_5.png](img_5.png)

##### User Profile with Images :
`curl --location 'http://localhost:8080/api/users/me/profile' \
--header 'Authorization: Basic dXNlcl9vbmU6cGFzc3dvcmQhMA=='`

Sample Out Put :
![img.png](img.png)




