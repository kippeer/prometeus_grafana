import React from 'react';
import { useQuery } from 'react-query';
import axios from 'axios';
import { Line } from 'react-chartjs-2';
import { format } from 'date-fns';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
} from 'chart.js';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

interface NetworkStatus {
  destination: string;
  latency: number;
  reachable: boolean;
  timestamp: string;
  packetsSent: number;
  packetsLost: number;
}

const NetworkDashboard: React.FC = () => {
  const { data: statusData, isLoading, error } = useQuery<NetworkStatus[]>(
    'networkStatus',
    async () => {
      const response = await axios.get('http://localhost:8080/api/network/status');
      return response.data;
    },
    {
      refetchInterval: 60000, // Refresh every minute
    }
  );

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-xl text-gray-600">Loading...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
        Error loading network status
      </div>
    );
  }

  const chartData = {
    labels: statusData?.map(status => format(new Date(status.timestamp), 'HH:mm:ss')) || [],
    datasets: [
      {
        label: 'Latency (ms)',
        data: statusData?.map(status => status.latency) || [],
        borderColor: 'rgb(75, 192, 192)',
        tension: 0.1,
      },
    ],
  };

  return (
    <div className="space-y-6">
      <div className="bg-white shadow rounded-lg p-6">
        <h2 className="text-xl font-semibold mb-4">Network Status Overview</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {statusData?.map((status) => (
            <div
              key={status.destination}
              className={`p-4 rounded-lg ${
                status.reachable ? 'bg-green-50' : 'bg-red-50'
              }`}
            >
              <h3 className="font-medium">{status.destination}</h3>
              <div className="mt-2 space-y-1">
                <p>Latency: {status.latency.toFixed(2)}ms</p>
                <p>
                  Packet Loss:{' '}
                  {((status.packetsLost / status.packetsSent) * 100).toFixed(1)}%
                </p>
                <p>Status: {status.reachable ? 'Online' : 'Offline'}</p>
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="bg-white shadow rounded-lg p-6">
        <h2 className="text-xl font-semibold mb-4">Latency Trends</h2>
        <div className="h-64">
          <Line data={chartData} options={{ maintainAspectRatio: false }} />
        </div>
      </div>
    </div>
  );
};

export default NetworkDashboard;