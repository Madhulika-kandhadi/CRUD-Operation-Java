// src/lib/apiConnect.test.ts
import axios from 'axios';
import {
  fetchAllWidgets,
  fetchWidgetByName,
  createWidget,
  updateWidget,
  deleteWidget,
  Widget,
} from './apiConnect';


// Mock axios using Jest's native mocking
jest.mock('axios');
const mockedAxios = axios as jest.MockedObject<typeof axios>;

describe('apiConnect', () => {
  // Sample widget data for tests
  const sampleWidget: Widget = {
    name: 'Widget Jones',
    description: 'Keeps a diary',
    price: 9.95,
  };

  const sampleWidgetList: Widget[] = [sampleWidget];

  beforeEach(() => {
    // Reset mocks before each test
    mockedAxios.get.mockReset();
    mockedAxios.post.mockReset();
    mockedAxios.patch.mockReset();
    mockedAxios.delete.mockReset();
  });

  describe('fetchAllWidgets', () => {
    it('returns response data on success', async () => {
      mockedAxios.get.mockResolvedValueOnce({ data: sampleWidgetList });

      const result = await fetchAllWidgets();

      expect(mockedAxios.get).toHaveBeenCalledWith('/');
      expect(result).toEqual(sampleWidgetList);
    });

    it('throws error on rejection', async () => {
      const errorMessage = 'Server error';
      mockedAxios.get.mockRejectedValueOnce({
        response: { statusText: errorMessage },
      });

      await expect(fetchAllWidgets()).rejects.toThrow(errorMessage);
    });
  });

  describe('fetchWidgetByName', () => {
    it('returns widget data on success', async () => {
      mockedAxios.get.mockResolvedValueOnce({ data: sampleWidget });

      const result = await fetchWidgetByName(sampleWidget.name);

      expect(mockedAxios.get).toHaveBeenCalledWith(`/${encodeURIComponent(sampleWidget.name)}`);
      expect(result).toEqual(sampleWidget);
    });

    it('throws error on rejection', async () => {
      const errorMessage = 'Widget not found';
      mockedAxios.get.mockRejectedValueOnce({
        response: { data: { message: errorMessage } },
      });

      await expect(fetchWidgetByName('nonexistent')).rejects.toThrow(errorMessage);
    });
  });

  describe('createWidget', () => {
    it('posts widget data and resolves on success', async () => {
      mockedAxios.post.mockResolvedValueOnce({ data: {} });

      await createWidget(sampleWidget);

      expect(mockedAxios.post).toHaveBeenCalledWith('/', sampleWidget);
    });

    it('throws error on rejection', async () => {
      const errorMessage = 'Widget already exists';
      mockedAxios.post.mockRejectedValueOnce({
        response: { data: { message: errorMessage } },
      });

      await expect(createWidget(sampleWidget)).rejects.toThrow(errorMessage);
    });
  });

  describe('updateWidget', () => {
    it('patches partial widget data and resolves on success', async () => {
      const updates: Partial<Widget> = { description: 'Updated diary', price: 12.99 };
      mockedAxios.patch.mockResolvedValueOnce({ data: {} });

      await updateWidget(sampleWidget.name, updates);

      expect(mockedAxios.patch).toHaveBeenCalledWith(
        `/${encodeURIComponent(sampleWidget.name)}`,
        updates
      );
    });

    it('throws error on rejection', async () => {
      const errorMessage = 'Widget not found';
      mockedAxios.patch.mockRejectedValueOnce({
        response: { data: { message: errorMessage } },
      });

      await expect(updateWidget('nonexistent', { price: 10 })).rejects.toThrow(errorMessage);
    });
  });

  describe('deleteWidget', () => {
    it('deletes widget and resolves on success', async () => {
      mockedAxios.delete.mockResolvedValueOnce({ data: {} });

      await deleteWidget(sampleWidget.name);

      expect(mockedAxios.delete).toHaveBeenCalledWith(
        `/${encodeURIComponent(sampleWidget.name)}`
      );
    });

    it('throws error on rejection', async () => {
      const errorMessage = 'Widget not found';
      mockedAxios.delete.mockRejectedValueOnce({
        response: { data: { message: errorMessage } },
      });

      await expect(deleteWidget('nonexistent')).rejects.toThrow(errorMessage);
    });
  });
});