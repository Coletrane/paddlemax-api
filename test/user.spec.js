const server = require('../server.js')
const testReq = require('supertest')
const request = require('request-promise')
const db = require('../models')
const config = require('../config')
const moment = require('moment')
const assert = require('chai').assert
require('dotenv').config()

const route = '/v1/user'
const facebookTestUsersUrl = `${process.env.FACEBOOK_APP_ID}/accounts/test-users`
let testUsers
const buddyRich = {
  email: 'uzqgctllms_1517190488@tfbnw.net',
  facebookId: '101479574003978'
}
let maxUser
let accessToken

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
  })
  describe('Endpoint Tests', () => {
    describe('POST /user/login', () => {
      it('should not login because invalid Facebook token', (done) => {
        testReq(server)
          .post(`${route}/login`)
          .send({
            email: 'notacop@fbi.com',
            facebookAuthToken: 'lkjhsflkjsd;lfkjds'
          })
          .expect(400, done)
      })
      // TODO: figure out location and birthday url params
      it('should login because valid Facebook token', async () => {
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
        return testReq(server)
          .post(`${route}/login`)
          .send(maxUser)
          .expect(200)
      })
    })
    describe('GET /user/me', () => {
      it('should not get a user', (done) => {
        testReq(server)
          .get(`${route}/me`)
          .expect(401, done)
      })
      it('should get a user', async () => {
        const options = {
          method: 'POST',
          url: `https://${process.env.AUTH0_DOMAIN}/oauth/token`,
          headers: {'content-type': 'application/json'},
          body: {
            grant_type: 'client_credentials',
            client_id: process.env.AUTH0_TEST_CLIENT_ID,
            client_secret: process.env.AUTH0_TEST_CLIENT_SECRET,
            audience: process.env.AUTH0_AUDIENCE
          },
          json: true
        }
        await request(options, (err, res, body) => {
          if (!err) {
            accessToken = body.access_token
          }
        })
        // console.log(token)
        return testReq(server)
          .get(`${route}/me`)
          .set('Authorization', `Bearer ${accessToken}`)
          .send({email: buddyRich.email})
          .expect(200)
      })
    })
    describe('PATCH /user', () => {
      it('should not update user info because invalid auth token', () => {
        return testReq(server)
          .patch(route)
          .send({email: 'artblackey@finnesinyolady.com'})
          .expect(401)
      })
      it('should not update user info because no email', () => {
        return testReq(server)
          .patch(route)
          .set('Authorization', `Bearer ${accessToken}`)
          .expect(400)
      })
      it('should update user info', async () => {
        let user = {
          email: buddyRich.email,
          weightLbs: 160,
          birthday: moment('01/01/1989', 'MM/DD/YYYY').toDate(),
          location: 'Roanoke, VA'
        }

        await testReq(server)
          .patch(route)
          .set('Authorization', `Bearer ${accessToken}`)
          .send(user)
          .expect(200)
          .then((res) => {
            assert(res.body.location === user.location)
            assert(res.body.birthday === user.birthday.toISOString())
            assert(res.body.weightLbs === user.weightLbs)
          })

        return testReq(server)
          .patch(route)
          .set('Authorization', `Bearer ${accessToken}`)
          .send({
            email: user.email,
            weightLbs: 180
          })
          .expect(200)
          .then((res) => {
            assert(res.body.weightLbs === 180)
          })
      })
    })
  })
})
