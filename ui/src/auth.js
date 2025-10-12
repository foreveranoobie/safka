const jwt = require('jsonwebtoken')

const attachUserToLocals = (req, res, next) => {
  if (req.oidc && req.oidc.isAuthenticated()) {
    // Extract permissions from req.oidc.access_token, if present
    let permissions = jwt.decode(req.oidc.accessToken.access_token)['permissions']
    res.locals.user = {
      email: req.oidc.user.email,
      picture: req.oidc.user.picture,
      sub: req.oidc.user.sub,
      nickname: req.oidc.user.nickname,
      permissions: permissions
    }
  } else {
    res.locals.user = null
  }
  next()
}

module.exports = {
  attachUserToLocals
}
