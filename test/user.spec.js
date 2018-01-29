const server = require('../server.js')
const request = require('supertest')
const user = require('../models/User')

const route = '/v1/user'
const testFacebookUser = {

}

describe('User', () => {
  describe('Auth tests', () => {
      it('should be unauthorized', (done) => {
        request(server)
          .get(`${route}/me`)
          .expect(401, done)
      })
      it('should be authorized', (done) => {
        request(server)
          .post(`${route}/login`)
          .send({email: "artblakey@finessinyolady.com"})
          .expect(400, done)
      })
      it('should authorize with Facebook', (done) => {
        request(server)
          .post(`${route}/login`)
          .send()
      })
    })
})
