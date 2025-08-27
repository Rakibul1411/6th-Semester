const grpc = require('@grpc/grpc-js');
const protoLoader = require('@grpc/proto-loader');
const path = require('path');

// Load proto file
const PROTO_PATH = path.join(__dirname, 'proto', 'user.proto');
const packageDefinition = protoLoader.loadSync(PROTO_PATH, {
  keepCase: true,
  longs: String,
  enums: String,
  defaults: true,
  oneofs: true,
});

const userProto = grpc.loadPackageDefinition(packageDefinition).user;

// Create client
const client = new userProto.UserService(
  'localhost:50051',
  grpc.credentials.createInsecure()
);

// Utility function to handle gRPC calls with promises
function promisifyGrpcCall(method, request) {
  return new Promise((resolve, reject) => {
    method(request, (error, response) => {
      if (error) {
        reject(error);
      } else {
        resolve(response);
      }
    });
  });
}

// Test functions
async function testCreateUser() {
  console.log('\n=== Testing Create User ===');
  
  try {
    const response = await promisifyGrpcCall(client.createUser.bind(client), {
      name: 'John Doe',
      email: 'john.doe@example.com',
      password: 'securepassword123',
      phone: '+1234567890',
      role: 'user'
    });

    console.log('Create User Response:', JSON.stringify(response, null, 2));
    return response.user?.id;
  } catch (error) {
    console.error('Create User Error:', error.message);
    return null;
  }
}

async function testCreateAnotherUser() {
  console.log('\n=== Testing Create Another User ===');
  
  try {
    const response = await promisifyGrpcCall(client.createUser.bind(client), {
      name: 'Jane Smith',
      email: 'jane.smith@example.com',
      password: 'anotherpassword456',
      phone: '+0987654321',
      role: 'admin'
    });

    console.log('Create Another User Response:', JSON.stringify(response, null, 2));
    return response.user?.id;
  } catch (error) {
    console.error('Create Another User Error:', error.message);
    return null;
  }
}

async function testGetUser(userId) {
  console.log('\n=== Testing Get User ===');
  
  if (!userId) {
    console.log('No user ID provided, skipping get user test');
    return;
  }

  try {
    const response = await promisifyGrpcCall(client.getUser.bind(client), {
      id: userId
    });

    console.log('Get User Response:', JSON.stringify(response, null, 2));
  } catch (error) {
    console.error('Get User Error:', error.message);
  }
}

async function testUpdateUser(userId) {
  console.log('\n=== Testing Update User ===');
  
  if (!userId) {
    console.log('No user ID provided, skipping update user test');
    return;
  }

  try {
    const response = await promisifyGrpcCall(client.updateUser.bind(client), {
      id: userId,
      name: 'John Doe Updated',
      phone: '+1111111111',
      role: 'moderator'
    });

    console.log('Update User Response:', JSON.stringify(response, null, 2));
  } catch (error) {
    console.error('Update User Error:', error.message);
  }
}

async function testListUsers() {
  console.log('\n=== Testing List Users ===');
  
  try {
    const response = await promisifyGrpcCall(client.listUsers.bind(client), {
      page: 1,
      limit: 5,
      search: ''
    });

    console.log('List Users Response:', JSON.stringify(response, null, 2));
  } catch (error) {
    console.error('List Users Error:', error.message);
  }
}

async function testSearchUsers() {
  console.log('\n=== Testing Search Users ===');
  
  try {
    const response = await promisifyGrpcCall(client.listUsers.bind(client), {
      page: 1,
      limit: 10,
      search: 'john'
    });

    console.log('Search Users Response:', JSON.stringify(response, null, 2));
  } catch (error) {
    console.error('Search Users Error:', error.message);
  }
}

async function testDeleteUser(userId) {
  console.log('\n=== Testing Delete User ===');
  
  if (!userId) {
    console.log('No user ID provided, skipping delete user test');
    return;
  }

  try {
    const response = await promisifyGrpcCall(client.deleteUser.bind(client), {
      id: userId
    });

    console.log('Delete User Response:', JSON.stringify(response, null, 2));
  } catch (error) {
    console.error('Delete User Error:', error.message);
  }
}

async function testDuplicateEmail() {
  console.log('\n=== Testing Duplicate Email ===');
  
  try {
    const response = await promisifyGrpcCall(client.createUser.bind(client), {
      name: 'Duplicate User',
      email: 'john.doe@example.com', // Same email as first user
      password: 'password123',
      phone: '+5555555555',
      role: 'user'
    });

    console.log('Duplicate Email Response:', JSON.stringify(response, null, 2));
  } catch (error) {
    console.error('Duplicate Email Error:', error.message);
  }
}

async function testInvalidRequests() {
  console.log('\n=== Testing Invalid Requests ===');
  
  // Test create user with missing fields
  try {
    const response = await promisifyGrpcCall(client.createUser.bind(client), {
      name: 'Incomplete User',
      email: 'incomplete@example.com'
      // Missing password and phone
    });

    console.log('Invalid Create Response:', JSON.stringify(response, null, 2));
  } catch (error) {
    console.error('Invalid Create Error:', error.message);
  }

  // Test get non-existent user
  try {
    const response = await promisifyGrpcCall(client.getUser.bind(client), {
      id: '507f1f77bcf86cd799439011' // Non-existent ID
    });

    console.log('Non-existent User Response:', JSON.stringify(response, null, 2));
  } catch (error) {
    console.error('Non-existent User Error:', error.message);
  }
}

// Main test runner
async function runAllTests() {
  console.log('🚀 Starting gRPC User Management System Tests');
  console.log('============================================');

  try {
    // Wait a bit for server to be ready
    await new Promise(resolve => setTimeout(resolve, 2000));

    // Test CRUD operations
    const userId1 = await testCreateUser();
    const userId2 = await testCreateAnotherUser();
    
    await testGetUser(userId1);
    await testUpdateUser(userId1);
    await testListUsers();
    await testSearchUsers();
    
    // Test error cases
    await testDuplicateEmail();
    await testInvalidRequests();
    
    // Clean up by deleting test users
    await testDeleteUser(userId1);
    
    console.log('\n✅ All tests completed!');
    console.log('============================================');

  } catch (error) {
    console.error('❌ Test suite failed:', error);
  } finally {
    // Close the client connection
    setTimeout(() => {
      process.exit(0);
    }, 1000);
  }
}

// Command line interface
if (require.main === module) {
  const command = process.argv[2];
  
  switch (command) {
    case 'create':
      testCreateUser().then(() => process.exit(0));
      break;
    case 'list':
      testListUsers().then(() => process.exit(0));
      break;
    case 'search':
      const searchTerm = process.argv[3] || '';
      promisifyGrpcCall(client.listUsers.bind(client), {
        page: 1,
        limit: 10,
        search: searchTerm
      }).then(response => {
        console.log(JSON.stringify(response, null, 2));
        process.exit(0);
      });
      break;
    case 'get':
      const userId = process.argv[3];
      if (!userId) {
        console.error('Please provide user ID: node client.js get <userId>');
        process.exit(1);
      }
      testGetUser(userId).then(() => process.exit(0));
      break;
    case 'delete':
      const deleteId = process.argv[3];
      if (!deleteId) {
        console.error('Please provide user ID: node client.js delete <userId>');
        process.exit(1);
      }
      testDeleteUser(deleteId).then(() => process.exit(0));
      break;
    default:
      console.log('Running comprehensive test suite...');
      runAllTests();
  }
}

module.exports = {
  client,
  promisifyGrpcCall,
  testCreateUser,
  testGetUser,
  testUpdateUser,
  testListUsers,
  testDeleteUser
};
