import tweepy

consumer_key = 'h6xWpkPMQgR2p9SHGJj4pqea6'
consumer_secret = '08A6m63UFLgiSJNYiNUrDTSG6peALFnN4ESjcESwATFZ7uWTHa'
access_token = '442148580-PbPAFaZfpqZzKywVm1F64UKB30FD3EfDyW4ELAIw'
access_token_secret = 'xNHmbN3gC28MxUyQ98LGZt4RZ8jgd1uSKSsipsWOYNRTj' 

auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_token, access_token_secret)

api = tweepy.API(auth)

public_tweets = api.home_timeline()
for tweet in public_tweets:
    print tweet.text