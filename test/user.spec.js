const server = require('../server.js')
const request = require('supertest')
const user = require('../models/User')

const testUserValid = {
  email: "artblakey@finessinyolady.com",
  facebookAuthToken: "123456789"
}

describe('User', () => {
  describe('Auth tests', () => {
      it('should be unauthorized', (done) => {
        request(server)
          .get('/v1/user/me')
          .expect(401, done)
      })
      it('should be authorized', (done) => {
        request(server)
          .post('/v1/user/login')
          .send('user', testUserValid)
          .expect(200, done)
      })
    })
})
