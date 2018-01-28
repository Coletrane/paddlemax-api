const test = require('tape')
const app = require('../server')
const request = require('supertest')
const user = require('../models/User')

test('should not be authorizied', t => {
  request(app)
    .get('/v1/user/me')
    .expect(401)
    .end(err => {
      t.ifError(err)
      t.end()
    })
})
