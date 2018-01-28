const router = require('express').Router()

router.use('/v1', require('./user'))

module.exports = router
