const server = require('../server.js')
const testReq = require('supertest')
const request = require('request-promise')
const db = require('../models')
const config = require('../config')
const auth0 = require('auth0-js')
require('dotenv').config()

const route = '/v1/user'
const facebookTestUsersUrl = `${process.env.FACEBOOK_APP_ID}/accounts/test-users`
let testUsers
const buddyRich = {
  email: 'uzqgctllms_1517190488@tfbnw.net',
  facebookId: '101479574003978'
}
let maxUser

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
        .send({email: 'artblakey@finessinyolady.com'})
        .expect(400, done)
    })
    it('should not authorize with Facebook', (done) => {
      testReq(server)
        .post(`${route}/login`)
        .send({
          email: 'notacop@fbi.com',
          facebookAuthToken: 'lkjhsflkjsd;lfkjds'
        })
        .expect(400, done)
    })
    // TODO: figure out location and birthday url params
    xit('should authorize with Facebook', async () => {
      // Get this App's auth token from FB
      let appToken
      let appTokenUrl =
          config.constants.facebookBaseUrl + '/oauth/access_token' +
          '?client_id=' + process.env.FACEBOOK_APP_ID +
          '&client_secret=' + process.env.FACEBOOK_APP_SECRET +
          '&grant_type=client_credentials'
      await request
        .get(appTokenUrl, (err, res, body) => {
          if (!err) {
            appToken = JSON.parse(body).access_token
          }
        })

        // Get test users from FB
      let testUsersUrl =
          config.constants.facebookBaseUrl +
          '/' + config.constants.facebookApiVersion +
          '/' + facebookTestUsersUrl +
          '/?access_token=' + appToken
      await request
        .get(testUsersUrl, async (err, res, body) => {
          if (!err) {
            testUsers = JSON.parse(body).data
          }
        })

        // request this app
      let buddy = testUsers.find((user) => {
        return user.id === buddyRich.facebookId
      })
      maxUser = {
        email: buddyRich.email,
        facebookId: buddy.id,
        facebookAuthToken: buddy.access_token
      }
      testReq(server)
        .post(`${route}/login`)
        .send(maxUser)
        .expect(200)
    })
  })
  describe('Endpoint Tests', () => {
    describe('GET /user/me', () => {
      it('should not get a user', (done) => {
        testReq(server)
          .get(`${route}/me`)
          .expect(401, done)
      })
      // TODO: figure out jwt validation with auth0
      it('should get a user', async (done) => {
        console.log({
          domain: process.env.AUTH0_DOMAIN,
          clientID: process.env.AUTH0_TEST_CLIENT_ID,
          responseType: 'token id_token',
          audience: 'https://paddle-max.auth0.com/userinfo',
          scope: 'openid',
          redirectUri: '/'
        })
        const auth = new auth0.WebAuth({
          domain: process.env.AUTH0_DOMAIN,
          clientID: process.env.AUTH0_TEST_CLIENT_ID,
          responseType: 'token id_token',
          audience: `${process.env.AUTH0_AUDIENCE}/userinfo`,
          scope: 'openid',
          redirectUri: '/'
        })
        await auth.authorize()
        auth.parseHash((err, authResult) => {
          if (!err) {
            console.log(authResult)
          }
        })
        // eslint:disable
        // function handleAuthentication() {
        //   auth.parseHash((err, authResult) => {
        //
        //   })
        // }
        testReq(server)
          .get(`${route}/me`)
          .expect(200, done)
      })
    })
  })
})
