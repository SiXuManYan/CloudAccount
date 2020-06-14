package com.fatcloud.account.entity.oss

/**
 * Created by Wangsw on 2020/6/13 0013 22:25.
 * </br>
 *
 */
class SecurityTokenModel {

    var RegionId: String = ""
    var AccessBucketName: String = ""
    var AccessBucketNamePublic: String = ""

    var AccessKeyId: String = ""
    var AccessKeySecret: String = ""
    var SecurityToken: String = ""

    /**
     * 过期时间
     */
    var ExpireTimeSeconds: String = ""
    var StoreDir: String = ""

}

/*

{
    "msg": "ok",
    "code": "200",
    "data": {
    "AccessBucketName": "ftacloud-bucket-private",
    "SecurityToken": "CAISjwJ1q6Ft5B2yfSjIr5bvOtOHma5E0q2EbWfHpUECW8Rdoq3u1zz2IHtPfnZtCO0ds/83lWBS7PcclrldV5ZOQUvZYY5Z5Z9Q7AW9Ox0MVjF2q+5qsoasPETOISyZtZagXoeUZdfZfejXHDKgvyRvwLz8WCy/Vli+S/OggoJmadJlMWvVaiFdVpUsZWkEksIBMmbLPvuAKwPjhnGqbHBloQ1hk2hym8Pdp8SX8UjZl0aoiL1X9cbTWsH8MpE8YMcvDonkg7ApKvb7vXQOu0QQxsBfl7dZ/DrLhNaZDmRK7g+OW+iuqYY0dlAkN/VhRvUc/aimz6wmoJTdi438zxFQMaROTz+aXoml0DtnpA0cqVBQGoABgO7otmEhrCs56mrboAasOEkNGmXb3HKSvjd++Mgh56W/8wIwO4+zG02xbLc9TExOLHbDcBGYI8MVH8AhvYjK3lwcQ0YZR3ECOe2CxwXKaCtV0P5znIUuNZyh3AE6/dpkOFB7I4pLc0NP5WagQ3tstg0jv6P1Ba7z/1gOAuVHaQQ=",
    "AccessKeyId": "STS.NUZqi3tqeeoooAvAAWWKqMhE5",
    "AccessKeySecret": "CvrLHEgFty3AiLeBZKmweCbX9g914U78j4Zow4FYkTUo",
    "AccessBucketNamePublic": "ftacloud-bucket-public",
    "RegionId": "oss-cn-qingdao",
    "ExpireTimeSeconds": "3600",
    "StoreDir": "ftacloud-test"
}
}

*/
