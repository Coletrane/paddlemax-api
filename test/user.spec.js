const server = require('../server.js')
const testReq = require('supertest')
const request = require('request-promise')
const db = require('../models')
const config = require("../config")
require('dotenv').config()

const route = '/v1/user'
const facebookTestUsersUrl = `${process.env.FACEBOOK_APP_ID}/accounts/test-users`
const testFacebookUser = {
  facebookAuthToken: "EAAC5ZAV6cpyIBAHmSiZC6IvceD1JDgYSfS4ThD2Hzlo7IWUry8oOPGPzm4ZBpbJRnSoghGOHHzHZAECF5cm6zwwxIcqQ9ZAsilrhTwZAk63WavoqxXs6uGL2iGqJOrlFrXrJACrGh5eM4jvgZAhgc4F5QwRZCOv4n8gJeS6IppAWIPXiUz7zhABD4ZAxhw0ZCMegesPZAZB2SWdHYgvwGrxZA4fZBm",
  email: "uzqgctllms_1517190488@tfbnw.net",
  firstName: "Buddy",
  lastName: "Rich",
  birthday: "02/06/1981",
}

describe('User', () => {

  before(() => {
    return db.sequelize.sync()
  })

  describe('Auth tests', () => {
      it('should be unauthorized', (done) => {
        testReq(server)
          .get(`${route}/me`)
          .expect(401, done)
      })
      it('should be authorized', (done) => {
        testReq(server)
          .post(`${route}/login`)
          .send({email: "artblakey@finessinyolady.com"})
          .expect(400, done)
      })
      it('should not authorize with Facebook', (done) => {
        testReq(server)
          .post(`${route}/login`)
          .send({
            email: "notacop@fbi.com",
            facebookAuthToken: "lkjhsflkjsd;lfkjds"
          })
          .expect(400, done)

      })
    // TODO: figure out location and birthday url params
      it('should authorize with Facebook', async () => {

        // Get this App's auth token from FB
        let appToken
        let appTokenUrl =
          config.constants.facebookBaseUrl + '/oauth/access_token' +
          '?client_id=' + process.env.FACEBOOK_APP_ID +
          '&client_secret=' + process.env.FACEBOOK_APP_SECRET +
          '&grant_type=client_credentials'
        await request
          .get(appTokenUrl, (err, res, body) => {
            appToken = JSON.parse(body).access_token
          })

        // Get test users from FB
        let testUsers
        let testUsersUrl =
          config.constants.facebookBaseUrl +
          '/' + config.constants.facebookApiVersion +
          '/' + facebookTestUsersUrl +
          '/?access_token=' + appToken
        await request
          .get(testUsersUrl, async (err, res, body) => {
            testUsers = JSON.parse(body).data
          })

        // request this app
        let maxUser = {
          email: "natking@finnesinyolady.com",
          facebookId: testUsers[0].id,
          facebookAuthToken: testUsers[0].access_token
        }
        testReq(server)
          .post(`${route}/login`)
          .send(maxUser)
          .expect(200)
      })
    })
  describe('Endpoint Tests', () => {
    describe('GET /user/me', () => {
      it('should not get a user', () => {
        testReq(server)
          .get(`${route}/me`)
          .expect(404)
      })
      // TODO: figure out jwt validation with auth0
      it('should get a user', () => {
        testReq(server)
          .get(`${route}/me`)
          .expect()
      })
    })
  })
})
