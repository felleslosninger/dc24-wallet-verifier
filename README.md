# MATTR Verifier
This is a simple application that demonstrates how a credential verifier would be able to request and recieve the credential of a user, using the MATTR API.

The application creates a `Request Template`, detailing what information the verifier needs from the user in order to function properly. 
Using that template, the verifier creates a `Presentation Request` which is converted to a QR code for the user to scan with their MATTR Showcase Wallet. 
The `Presentation Request` contains a callback URL which MATTR uses to send the credential to the verifier.
By hosting the application on `fly.io`, the verifier is able recieve credentials from MATTR, which is then displayed to the user.

The requested claims in the `Request Template` is based on the claims our [Issuer](https://github.com/felleslosninger/dc24-eu-wallet) recieves from Ansattporten.
For details on hosting the verifier on `fly.io`, see [our explanation](https://github.com/felleslosninger/dc24-wallet-verifier/?tab=readme-ov-file#flyio-setup) as well as their [official documentation](https://fly.io/docs/getting-started/).
  
## To run application
Update the `.env.example` file with secrets specified there, and then rename it to `.env`.

The MATTR secrets can be obtained by requesting access to their API and following their [guide](https://learn.mattr.global/guides/) for setting up a verifier, as well as their [API documentation](https://learn.mattr.global/api-reference/).

## Fly.io Setup



## Presentation Flow

```mermaid
sequenceDiagram    
    participant UA as User Agent
    participant W as Wallet
    participant VE as Verifier Endpoints
    participant V as Verifier

    UA->>VE: Trigger presentation 
    
    VE->>+V: Request QR code
    V->>V: Generate QR code
    V->>+VE: Respond with QR code

    VE->>+UA: Display QR code
    UA->>W: Scan QR code and trigger wallet

    W->>+V: Send POST request
    V->>V: Update verification status
    V->>VE: Redirect if verified
    VE->>UA: Render wallet response 
```


## Endpoints
### Default Endpoint

- _Method_: GET
- _URL_: http://localhost:2001/

A default endpoint serving as the verifier's homepage, containing a button which redirects the user to the QR code. If a QR code does not already exist in `/resources/static/qrCodes/`, the application first generates a QR code before redirecting the user.


### QR Code Endpoint

- _Method_: GET
- _URL_: http://localhost:2001/qr-code

An endpoint serving the QR code stored in `/resources/static/qrCodes/`. The QR code can be scanned by the MATTR Showcase Wallet, causing a `Selective Disclosure` `Presentation` to appear in the wallet asking for credentials. In order for the verifier to request correct information, ensure that the credential details requested in the `Request Template` are present in your `Verifiable Credential`.

If no QR code is present in the `qrCodes/` folder, it is generated the first time this endpoint is accessed. If the page fails to find the QR code image, relaunching the application fixes this issue, as the application struggles to access files generated while the program is running (non pre-generated files).


### Callback Endpoint

- _Method_: POST
- _URL_: http://localhost:2001/callback

An endpoint for MATTR to Post the user's credentials to. In order for MATTR to be able to access this URL it has to be included in the `Presentation Request`. When recieving a POST request, the verifier sets `hasReceivedVP` to `true`, as this would mean MATTR has been able to send a request to the callback URL.


### Verification Status Endpoint

- _Method_: GET
- _URL_: http://localhost:2001/verification-status

An endpoint which checks the status of `hasReceivedVP`, which gets updated in the [Callback Endpoint](https://github.com/felleslosninger/dc24-wallet-verifier/?tab=readme-ov-file#callback-endpoint). As the user's browser is still at the [QR Code Endpoint](https://github.com/felleslosninger/dc24-wallet-verifier/?tab=readme-ov-file#qr-code-endpoint) after scanning the QR code, the html has javascript code that polls this endpoint, checking if `hasReceivedVP` is true. If so the javascript redirects to the [Presentation View Endpoint](https://github.com/felleslosninger/dc24-wallet-verifier/?tab=readme-ov-file#presentation-view-endpoint)


### Presentation View Endpoint

- _Method_: GET
- _URL_: http://localhost:2001/presentation-view

Simple endpoint that displays the information retrieved from the user's credential.
