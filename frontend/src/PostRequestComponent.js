import React from 'react';

const sendPostRequest = async () => {
  const url = 'http://localhost:2001/callback';
  const data = {
    presentationType: "QueryByFrame",
    challengeId: "GW8FGpP6jhFrl37yQZIM6w",
    claims: {
      id: "did:key:z6MkfxQU7dy8eKxyHpG267FV23agZQu9zmokd8BprepfHALi",
      "http://schema.org/familyName": "Shin",
      "http://schema.org/educationalCredentialAwarded": "Certificate Name"
    },
    verified: true,
    holder: "did:key:z6MkgmEkNM32vyFeMXcQA7AfQDznu47qHCZpy2AYH2Dtdu1d"
  };

  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    });

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    const responseData = await response.json();
    console.log('Response data:', responseData);
  } catch (error) {
    console.error('Error:', error);
  }
};

const PostRequestComponent = () => {
  return (
    <div>
      <button onClick={sendPostRequest}>Send POST Request</button>
    </div>
  );
};

export default PostRequestComponent;