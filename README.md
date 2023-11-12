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
`
{
"name": "user_one",
"password": "password!0",
"imageIds":[],
"error": ""
}
`
##### Upload Image :
`curl --location 'http://localhost:8080/api/image/upload' \
--header 'image_info: {"title":"Imgae Tile G","description":"Description for Imag"}' \
--header 'Authorization: Basic dXNlcl9vbmU6cGFzc3dvcmQhMA==' \
--form 'image=@"/C:/Users/Niranjan Nallapu/OneDrive/Desktop/cat.jfif"'`

Sample Out Put :

`
{
"id": "1lZ0PLT",
"title": "Imgae Tile 2",
"description": "Description for Imag2",
"datetime": 1699812545,
"type": "image/jpeg",
"animated": false,
"width": 306,
"height": 204,
"size": 13268,
"views": 0,
"bandwidth": 0,
"vote": null,
"favorite": false,
"nsfw": null,
"section": null,
"account_url": null,
"account_id": 175873757,
"is_ad": false,
"in_most_viral": false,
"has_sound": false,
"tags": [],
"ad_type": 0,
"ad_url": "",
"edited": "0",
"in_gallery": false,
"deletehash": "3v8EXe5FO5um4mn",
"name": "",
"link": "https://i.imgur.com/1lZ0PLT.jpg"
}
`
##### Get Image :
`curl --location 'http://localhost:8080/api/image/Yz5pYBO' \
--header 'Authorization: Basic dXNlcl9vbmU6cGFzc3dvcmQhMA==' \`

Sample Out Put :

`
{
"data": {
"id": "Yz5pYBO",
"title": "Image Title",
"description": "Image Description",
"datetime": 1699811804,
"type": "image/jpeg",
"animated": false,
"width": 306,
"height": 204,
"size": 13268,
"views": 0,
"bandwidth": 0,
"vote": null,
"favorite": false,
"nsfw": false,
"section": null,
"account_url": null,
"account_id": 175873757,
"is_ad": false,
"in_most_viral": false,
"has_sound": false,
"tags": [],
"ad_type": 0,
"ad_url": "",
"edited": "0",
"in_gallery": false,
"deletehash": "Sd5WNy4YrQciBeg",
"name": null,
"link": "https://i.imgur.com/Yz5pYBO.jpg",
"ad_config": {
"safeFlags": [
"not_in_gallery",
"share"
],
"highRiskFlags": [],
"unsafeFlags": [
"sixth_mod_unsafe"
],
"wallUnsafeFlags": [],
"showsAds": false,
"showAdLevel": 1,
"safe_flags": [
"not_in_gallery",
"share"
],
"high_risk_flags": [],
"unsafe_flags": [
"sixth_mod_unsafe"
],
"wall_unsafe_flags": [],
"show_ads": false,
"show_ad_level": 1,
"nsfw_score": 0
}
},
"success": true,
"status": 200
}
`
##### Update Image :
`curl --location 'http://localhost:8080/api/image/Yz5pYBO' \
--header 'Authorization: Basic dXNlcl9vbmU6cGFzc3dvcmQhMA==' \
--form 'title="Image Title"' \
--form 'description="Image Description"'`

Sample Out Put :

`{
"data": true,
"success": true,
"status": 200
}`

##### Delete Image :
`curl --location --request DELETE 'http://localhost:8080/api/image/Yz5pYBO/delete' \
--header 'Authorization: Basic dXNlcl9vbmU6cGFzc3dvcmQhMA=='`

Sample Out Put :

`{
"data": true,
"success": true,
"status": 200
}`

##### User Profile with Images :
`curl --location 'http://localhost:8080/api/users/me/profile' \
--header 'Authorization: Basic dXNlcl9vbmU6cGFzc3dvcmQhMA=='`

Sample Out Put :

`
{
"data": [
{
"id": "K8qzbPx",
"title": "Imgae Tile G",
"description": "Description for Imag",
"datetime": 1699812520,
"type": "image/jpeg",
"animated": false,
"width": 306,
"height": 204,
"size": 13268,
"views": 0,
"bandwidth": 0,
"vote": null,
"favorite": false,
"nsfw": null,
"section": null,
"account_url": "NNallap",
"account_id": 175873757,
"is_ad": false,
"in_most_viral": false,
"has_sound": false,
"tags": [],
"ad_type": 0,
"ad_url": "",
"edited": "0",
"in_gallery": false,
"deletehash": "TyvmkBgEG8AcZe4",
"name": null,
"link": "https://i.imgur.com/K8qzbPx.jpg"
},
{
"id": "k6hlDRE",
"title": "Imgae Tile G",
"description": "Description for Imag",
"datetime": 1699812523,
"type": "image/jpeg",
"animated": false,
"width": 306,
"height": 204,
"size": 13268,
"views": 0,
"bandwidth": 0,
"vote": null,
"favorite": false,
"nsfw": null,
"section": null,
"account_url": "NNallap",
"account_id": 175873757,
"is_ad": false,
"in_most_viral": false,
"has_sound": false,
"tags": [],
"ad_type": 0,
"ad_url": "",
"edited": "0",
"in_gallery": false,
"deletehash": "Rxgk1fTUUYjwPy6",
"name": null,
"link": "https://i.imgur.com/k6hlDRE.jpg"
},
{
"id": "1lZ0PLT",
"title": "Imgae Tile 2",
"description": "Description for Imag2",
"datetime": 1699812545,
"type": "image/jpeg",
"animated": false,
"width": 306,
"height": 204,
"size": 13268,
"views": 0,
"bandwidth": 0,
"vote": null,
"favorite": false,
"nsfw": null,
"section": null,
"account_url": "NNallap",
"account_id": 175873757,
"is_ad": false,
"in_most_viral": false,
"has_sound": false,
"tags": [],
"ad_type": 0,
"ad_url": "",
"edited": "0",
"in_gallery": false,
"deletehash": "3v8EXe5FO5um4mn",
"name": null,
"link": "https://i.imgur.com/1lZ0PLT.jpg"
}
],
"profile": "user_one"
}
`

Exception Handling : 
==================
If user is Unauthorized trying to access the endpoints :

`{
"errorCode": "401 UNAUTHORIZED",
"errorMessage": "Full authentication is required to access this resource"
}`

Other than Unauthorized any kind of exception will display in above json format only.

Kafka Event Publishing :
=======================
After completion of Image upload kafka event publisher publush the event to
kafka topic : **profile_topic**

Event Details :

`{
"id": 1,
"username": "user_one",
"password": "$2a$10$6hLBtZ/6pOyG9F30yUASsuTqKWqL8Kk0u3hxsOTqgGuP0SIB6dKY.",
"imageIds": [
"Yz5pYBO",
"K8qzbPx",
"k6hlDRE",
"1lZ0PLT"
],
"roles": []
}`

**CI/CD Integration with GitHub Actions :**

 GitHub providing actions for CI/CD integration. this one is simple Docker workflow
integration which will build and publish images to public docker hub.

Workflow Yml :
`
name: Publish Docker image

on:
push:
branches: ['master']

jobs:
push_to_registry:
name: Push Docker image to Docker Hub
runs-on: ubuntu-latest
steps:
- name: Check out the repo
uses: actions/checkout@v3

      - name: Log in to Docker Hub
        uses: docker/login-action@f054a8b539a109f9f41c372932f1ae047eff08c9
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_PASSWORD }}
      
      - name: Extract metadata (tags, labels) for Docker
        id: meta
        uses: docker/metadata-action@98669ae865ea3cffbcbaa878cf57c20bbf1c6c38
        with:
          images: nnallapu/imageapi
      
      - name: Build and push Docker image
        uses: docker/build-push-action@ad44023a93711e3deb337508980b4b5e9bcdc5dc
        with:
          context: .
          push: true
          tags: ${{ steps.meta.outputs.tags }}
          labels: ${{ steps.meta.outputs.labels }}
`

